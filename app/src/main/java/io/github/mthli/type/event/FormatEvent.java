/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mthli.type.event;

public class FormatEvent {
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;
    private boolean isStrikethrough;
    private boolean isLink;

    public FormatEvent() {}

    public FormatEvent(boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrikethrough, boolean isLink) {
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isLink = isLink;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    public boolean isStrikethrough() {
        return isStrikethrough;
    }

    public void setStrikethrough(boolean isStrikethrough) {
        this.isStrikethrough = isStrikethrough;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean isLink) {
        this.isLink = isLink;
    }
}
